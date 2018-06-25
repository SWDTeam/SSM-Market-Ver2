Rails.application.routes.draw do
  devise_for :accounts

  resources :categories
  resources :images
  resources :products
  resources :order_products
  resources :orders
  resources :roles
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

  root to: redirect("/accounts/sign_in")
 
end
