Rails.application.routes.draw do
  devise_for :accounts,  controllers: {
    sessions: 'accounts/sessions',
    passwords: 'accounts/passwords',
    registrations: 'accounts/registrations',
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

  get '/change_password/new', to: 'accounts#change_password', as: 'change_password'
  post '/change_password/:account_id/edit', to: 'accounts#edit_password', as: 'edit_change_password'

  get '/home', to: 'products#home', as: "home" 
  # post '/change_pass' 
  devise_scope :account do
    root "accounts/sessions#new"
  end

#---------------Mobile api
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
      # categories
      resources :categories, only: [:index]
      #products
      resources :products, only: [:show, :index]
    end
  end

end
