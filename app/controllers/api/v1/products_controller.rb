class Api::V1::ProductsController < ActionController::API
  def index_products_by_category_id
    h = Hash.new
    results = Array.new
    @lists = Product.includes(:images).where(category_id: params[:category_id], status: 'active')

    if @lists.size < 1
      render json: {errors: 'Not found'}, status: 404
    else
      @lists.each do |p|
        results.push({id: p.id, name: p.name, url: p.images.first.url.url, 
          product_key: p.product_key, quantity: p.quantity, manufacturer: p.manufacturer,
          manu_date: p.manu_date, expired_date: p.expired_date, description: p.description,
          price: p.price, status: p.status, category_id: p.category_id
        })
      end
      h[:products] = results
      render json: h.as_json
    end
  end

  def index
    h = Hash.new
    t = Array.new
    @products = Product.includes(:images).where(status: 'active').order(:created_at).last(2)
    @products.each do |p|
      cate_name = Category.find(p.category_id).name
      t.push({id: p.id, name: p.name, url: p.images.first.url.url, 
        product_key: p.product_key, quantity: p.quantity, manufacturer: p.manufacturer,
        manu_date: p.manu_date, expired_date: p.expired_date, description: p.description,
        price: p.price, status: p.status, category_id: p.category_id, category_name: cate_name
      })
    end
    h[:products] = t
    render json: h.as_json
  end
  


  def show
    product = Product.find_by_id(params[:id])
    name = Category.find(product.category_id).name
    unless product.nil?
      render json: product.as_json.merge({category_name: name}).to_json, status: 200 
      return      
    end
    render json: {errors: 'Not found'}, status: 404
  end

 
  

end