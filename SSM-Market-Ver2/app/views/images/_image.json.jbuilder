json.extract! image, :id, :url, :url_out, :created_at, :updated_at
json.url image_url(image, format: :json)
