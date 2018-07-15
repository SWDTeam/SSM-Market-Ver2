class ProductsController < ApplicationController
  before_action :set_product, only: [:show, :edit, :update, :destroy]
  before_action :authenticate_account!

  def home
    @products = Product.all.size
    @orders = Order.all
    @revenue = 0
    @orders.each do |p|
      @revenue += p.total_price
    end
    @users = Account.all.size

    id = OrderProduct.order("quantity DESC").take(3).pluck(:product_id).uniq
 
    @best_products = Product.where(id: id)
    # @best_products.each do |pid|
    #   @p = OrderProduct.where(product_id: pid)
    #   q = 0
    #   @p.each do |i|
    #     q += i.quantity
    #   end
    #   puts q
    # end 

    # vip customer
    money = 0
    vip_id = Order.order("total_price DESC").distinct.pluck(:account_id).take(3)
    @vip_customers = Account.where(id: vip_id)
    # @vip_customers.each do |cus|
    #   @t = Order.where(account_id: cus.id)
    #   @t.each do |i|
    #     money += i.total_price
    #   end
    #   # puts money
    # end
  end

  # GET /products
  # GET /products.json
  def index
    @products = Product.all.page(params[:page]).per(5)
    @categories = Category.all
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
    @categories = Category.all
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
    @categories = Category.all
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
      @products = Product.where(status: params[:status])
      result = Array.new
      @products.each do |t|
        result.push({id: t.id, name: t.name, quantity: t.quantity, 
          status: t.status, category_name: Category.find_by_id(t.category_id).name })
      end
      render json: result.to_json
    end
  end

  def search_products_by_category
    if params[:category_id]
      @products = Product.where(category_id: params[:category_id])      
      result = Array.new
      
      @products.each do |t|
        result.push({id: t.id, name: t.name, quantity: t.quantity, 
          status: t.status, category_name: Category.find_by_id(params[:category_id]).name })
      end
      # puts result.to_json
      render json: result.to_json
    end
  end

  def search_products_by_name
    if params[:name]
      @products = Product.where("name LIKE ?", "%#{params[:name]}%")
      result = Array.new
      @products.each do |t|
        result.push({id: t.id, name: t.name, quantity: t.quantity, 
          status: t.status, category_name: Category.find_by_id(t.category_id).name })
      end

      render json: result.to_json
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
