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
  resources :accounts
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

  get '/home/:id', to: 'products#home', as: "home"  

  namespace :api do
    namespace :v1 do
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
