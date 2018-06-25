json.extract! product, :id, :product_key, :name, :quantity, :manufacturer, :manu_date, :expired_date, :description, :price, :status, :created_at, :updated_at
json.url product_url(product, format: :json)
