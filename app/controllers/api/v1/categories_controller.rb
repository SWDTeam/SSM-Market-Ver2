class Api::V1::CategoriesController < ActionController::API
  
  def index
    h = Hash.new
    results = Array.new
    @categories = Category.all.includes(:images)
    @categories.each do |c|
      results.push({id: c.id, name: c.name, url: c.images.first.url.url})
    end
    h[:categories] = results
    render json: h.as_json
  end
  
  
end