class ProductsController < ApplicationController
  before_action :set_product, only: [:show, :edit, :update, :destroy]
  before_action :authenticate_account!
  def home

  end

  # GET /products
  # GET /products.json
  def index
    @products = Product.all
    @categories = Category.all.includes(:images)
  end

  # GET /products/1
  # GET /products/1.json
  def show
  end

  # GET /products/new
  def new
    @categories = Category.all
    @product = Product.new
    @image = @product.images.build
  end

  # GET /products/1/edit
  def edit
    @categories = Category.all
  end

  # POST /products
  # POST /products.json
  def create
    @user = current_account
    # @product = Product.new(product_params)
    @product = @user.products.build(product_params)
    
    respond_to do |format|
      if @product.save
        unless params[:images].nil?
          params[:images]['url'].each do |img|
            @image = @product.images.create!(url: img, product_id: @product.id)
          end
        end
        format.html { redirect_to product_path(@product), notice: 'Product was successfully created.' }
        format.json { render :show, status: :created, location: @product }
      else
        format.html { render :new }
        format.json { render json: @product.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /products/1
  # PATCH/PUT /products/1.json
  def update
    respond_to do |format|
      if @product.update(product_params)
        unless params[:images].nil?
          params[:images]['url'].each do |i|
            @image = @product.images.create!(url: i, product_id: @product.id)
          end
        end
        unless params[:images_delete].nil?
          params[:images_delete].each do |i|
            image = Image.find(i['id'])
            @product.images.destroy(image)
          end
        end
        format.html { redirect_to product_path(@product), notice: 'Product was successfully updated.' }
        format.json { render :show, status: :ok, location: @product }
      else
        format.html { render :edit }
        format.json { render json: @product.errors, status: :unprocessable_entity }
      end 
    end
  end

  # DELETE /products/1
  # DELETE /products/1.json
  def destroy
    @product.destroy
    respond_to do |format|
      format.html { redirect_to products_url, notice: 'Product was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  def search_products_by_status
    if params[:status]
      @products = Product.select(:id).where(status: params[:status])
      render json: @products
    end
  end
  

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_product
      @product = Product.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def product_params
      params.require(:product).permit(:product_key, :name, :quantity, :manufacturer, 
        :manu_date, :expired_date, :description, :price, :status, :category_id, :updater_id)
    end
end
