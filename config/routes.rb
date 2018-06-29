Rails.application.routes.draw do
  devise_for :accounts,  controllers: {
    sessions: 'accounts/sessions',
    passwords: 'accounts/passwords',
    registrations: 'accounts/registrations',
  }
  resources :categories
  resources :images
  resources :products
  resources :order_products
  resources :orders
  resources :roles
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

  get '/home', to: 'products#home', as: "home"  

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

  devise_scope :account do
    root "accounts/sessions#new"
  end
end
