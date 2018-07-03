class CategoriesController < ApplicationController
  before_action :set_category, only: [:show, :edit, :update, :destroy]
  before_action :authenticate_account!
  # GET /categories
  # GET /categories.json
  def index
    @categories = Category.all.includes(:images)
  end

  # GET /categories/1
  # GET /categories/1.json
  def show
  end

  # GET /categories/new
  def new
    @category = Category.new
    @image = @category.images.build
  end

  # GET /categories/1/edit
  def edit
  end

  # POST /categories
  # POST /categories.json
  def create
    @user = current_account
    # @category = Category.new(category_params)
    @category = @user.categories.build(category_params)
    respond_to do |format|
      if @category.save
        unless params[:images].nil?
          params[:images]['url'].each do |img|
            @image = @category.images.create!(url: img, category_id: @category.id)
          end
        end
        format.html { redirect_to categories_path, notice: 'Category was successfully created.' }
        format.json { render :index, status: :created, location: @category }  
      else
        format.html { render :new }
        format.json { render json: @category.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /categories/1
  # PATCH/PUT /categories/1.json
  def update
    respond_to do |format|
      if @category.update(category_params)
        unless params[:images].nil?
          params[:images]['url'].each do |i|
            @image = @category.images.create!(url: i, category_id: @category.id)
          end
        end
        unless params[:images_delete].nil?
          params[:images_delete].each do |i|
            image = Image.find(i['id'])
            @category.images.destroy(image)
          end
        end
        format.html { redirect_to categories_path, notice: 'Category was successfully updated.' }
        format.json { render :show, status: :ok, location: @category }
      else
        format.html { render :edit }
        format.json { render json: @category.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /categories/1
  # DELETE /categories/1.json
  def destroy
    @category.destroy
    respond_to do |format|
      format.html { redirect_to categories_url, notice: 'Category was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_category
      @category = Category.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def category_params
      params.require(:category).permit(:name, :status)
    end
end
