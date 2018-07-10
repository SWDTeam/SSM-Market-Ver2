class Api::V1::OrderProductsController < ActionController::API
  def index_order_products_by_order_id
    if params[:order_id]
      results = Array.new
      @details = OrderProduct.where(order_id: params[:order_id])
      @details.each do |d|
        results.push({
          order_products_id: d.id,
          product_id: d.product_id,
          product_name: Product.find_by_id(d.product_id).name,
          url: Image.find_by_product_id(d.product_id).url.url,
          price: d.price, 
          quantity: d.quantity,
          total: d.price * d.quantity,
          status: d.status,
          reason: d.reason 
        })
      end
      if @details.size < 1
        render json: {errors: 'Not found'}, status: 404
      else
        render json: results.as_json
      end
    end
  end
end