json.extract! order, :id, :code, :payment_date, :address_ship, :total_price, :status, :total_quantity, :created_at, :updated_at
json.url order_url(order, format: :json)
