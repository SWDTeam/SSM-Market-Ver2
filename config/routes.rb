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
  resources :categories, only: [:index]
  resources :order_products
  resources :orders
  resources :roles

  get '/home', to: 'products#home', as: "home"  
  devise_scope :account do
    root "accounts/sessions#new"
  end

#---------------Mobile api
  namespace :api do
    namespace :v1 do
      resources :accounts, only: [:edit, :update, :show]
      post "change_password", to: "accounts#change_password"
      devise_scope :account do
        post "sign_up", :to => 'registrations#create'
        post "sign_in", :to => 'sessions#create'
        delete "sign_out", :to => 'sessions#destroy'
      end
    end
  end

end
