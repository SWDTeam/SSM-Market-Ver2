class OrdersController < ApplicationController
  before_action :set_order, only: [:show, :edit, :update, :destroy]

  # GET /orders
  # GET /orders.json
  def index
    @orders = Order.all.page(params[:page]).per(5)
  end

  # GET /orders/1
  # GET /orders/1.json
  def show
    @order = Order.find_by_id(params[:id])
    @account = Account.find_by_id(@order.account_id)
    @order_products = OrderProduct.where(order_id: @order.id)
  end

  # GET /orders/new
  def new
    @order = Order.new
  end

  # GET /orders/1/edit
  def edit
  end

  # POST /orders
  # POST /orders.json
  def create
    @order = Order.new(order_params)

    respond_to do |format|
      if @order.save
        format.html { redirect_to @order, notice: 'Order was successfully created.' }
        format.json { render :show, status: :created, location: @order }
      else
        format.html { render :new }
        format.json { render json: @order.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /orders/1
  # PATCH/PUT /orders/1.json
  def update
    respond_to do |format|
      if @order.update(order_params)
        format.html { redirect_to orders_path, notice: 'Order was successfully updated.' }
        format.json { render :index, status: :ok, location: @order }
      else
        format.html { render :edit }
        format.json { render json: @order.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /orders/1
  # DELETE /orders/1.json
  def destroy
    @order.destroy
    respond_to do |format|
      format.html { redirect_to orders_url, notice: 'Order was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  def seach_orders_by_status
    if params[:status]
      @orders = Order.where(status: params[:status])
      result = Array.new
      @orders.each do |t|
        result.push({id: t.id, code: t.code, cus_name: Account.find_by_id(t.account_id).name,
          status: t.status, total_price: t.total_price})
      end
      render json: result.to_json
    end

  end
  
  def search_orders_by_code
    if params[:code]
      @orders = Order.where("code LIKE ?", "%#{params[:code]}%")
      result = Array.new
      @orders.each do |t|
        result.push({id: t.id, code: t.code, cus_name: Account.find_by_id(t.account_id).name,
          status: t.status, total_price: t.total_price})
      end
      render json: result.to_json
    end
  
  end
  

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_order
      @order = Order.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def order_params
      params.require(:order).permit(:code, :payment_date, :address_ship, :total_price, :status, :total_quantity, :cashier_id)
    end
end
