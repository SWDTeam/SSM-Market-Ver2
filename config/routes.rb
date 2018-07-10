Rails.application.routes.draw do
  devise_for :accounts,  controllers: {
    sessions: 'accounts/sessions',
    passwords: 'accounts/passwords',
    # registrations: 'accounts/registrations',
  }
  resources :accounts do
    resources :categories, only: [:new, :edit, :update, :destroy, :create]  do 
      resources :images, only: [:create, :edit, :update, :show, :destroy]
    end
    resources :products, only: [:new, :edit, :update, :destroy, :create] do
      resources :images, only: [:create, :edit, :update, :show, :destroy]
    end
  end
  resources :products, only: [:index, :show]
  resources :categories, only: [:index]
  resources :order_products
  resources :orders
  resources :roles

  get '/change_password/:id/new', to: 'accounts#edit_change_password', as: 'change_password'
  post '/change_password/:id', to:'accounts#change_password'

  get '/home', to: 'products#home', as: "home" 
  # post '/change_pass' 
  devise_scope :account do
    root "accounts/sessions#new"
  end
#---------------Ajax---------------
  get '/account_by_role', to: 'accounts#search_accounts_by_role'
  get '/account_by_name', to: 'accounts#search_accounts_by_name'

  get '/products_by_status', to: "products#search_products_by_status"
  get '/products_by_category', to: "products#search_products_by_category"
  get '/products_by_name', to: "products#search_products_by_name"

  get '/categories_by_name', to: "categories#search_categories_by_name"
#---------------Mobile api---------------
  namespace :api do
    namespace :v1 do
      # accounts
      resources :accounts, only: [:edit, :update, :show] 

      post "change_password", to: "accounts#change_password"
      devise_scope :account do
        post "sign_up", :to => 'registrations#create'
        post "sign_in", :to => 'sessions#create'
        delete "sign_out", :to => 'sessions#destroy'
      end
      # order products
      resources :orders, only: [:create, :destroy]
      get 'lists_orders/:account_id', to: 'orders#index_orders_by_account_id' # find list orders by account id and status pending
      get 'order_details/:order_id', to: 'order_products#index_order_products_by_order_id'
      # resources :order_products, only: [:create]
      # categories
      resources :categories, only: [:index]
      get 'search_category_name/:category_name', to: "categories#search_categories_by_name"      
      #products
      resources :products, only: [:show, :index]
      get 'list_products/:category_id', to: "products#index_products_by_category_id"  #find list product by category id
      get 'products_barcode/:product_key', to: "products#search_barcode"
    end
  end

end
