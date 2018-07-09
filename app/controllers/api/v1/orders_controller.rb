class Api::V1::OrdersController < ActionController::API
  before_action :ensure_params_exist, only: :create
  before_action :check_quantity, only: :create
  respond_to :json

  def create
    order_detail = params[:order_product]
    code = Random.new
    # get account user buy
    @user = Account.find_by_id(params[:order][:account_id])
    @order = Order.new

    # cal total price, total quantity in order
    price = 0
    quantity = 0
    order_detail.each_with_index do |detail|
      quantity += detail[:quantity]
      price += detail[:price] * detail[:quantity]
    end

    params[:order][:code] = code.rand(1000000...9999999).to_s  # random code order
    params[:order][:total_price] = price
    params[:order][:total_quantity] = quantity
    @order = @user.orders.build(order_params)

    t = Array.new # json reponse
    if @order.save
      order_detail.each_with_index do |detail, index|
        @order_product = @order.order_products.create!(product_id: detail[:product_id],
          quantity: detail[:quantity], price: detail[:price])
        t[index] = @order_product
        #update new quantity in Product
        product = Product.find_by_id(detail[:product_id])
        quantity = product.quantity - detail[:quantity]
        product.update_attributes(quantity: quantity)
      end
    end

    render json: {order: @order, order_product: t}
  end

  def index_orders_by_account_id
    if params[:account_id]
      @orders = Order.where(account_id: params[:account_id], status: 'pending')
      return render json: {errors: 'Not found'}, status: 404 if @orders.size < 1
      render json: @orders.as_json
    end 
  end

  protected
  def ensure_params_exist
    if params[:order][:address_ship].blank? || params[:order][:account_id].blank? || params[:order_product].size < 1
      render json: {success: false, message: "missing orders parameter"}, status: 422 
    end
    
  end

  # check quantity in db have enough amount, price json is match with db?
  def check_quantity
    order_detail = params[:order_product]
    order_detail.each_with_index do |detail|
      product = Product.find_by_id(detail[:product_id])
      return render json: product.as_json.merge({success: false}).to_json if product.quantity - detail[:quantity] < 0 || detail[:quantity] < 1
      return render json: {success: false, message: "price not match"} if product.price != detail[:price]
    end
  end

  private
  def order_params
    params.require(:order).permit(:code, :payment_date, 
      :address_ship, :total_price, :status, 
      :total_quantity, :account_id, :cashier_id)
  end

  def order_product_params
    params.require(:order_product).permit(:price, :quantity, :reason, 
      :status, :order_id, :product_id)
  end
end




  
