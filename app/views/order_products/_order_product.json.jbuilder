json.extract! order_product, :id, :price, :quantity, :reason, :status, :created_at, :updated_at
json.url order_product_url(order_product, format: :json)
