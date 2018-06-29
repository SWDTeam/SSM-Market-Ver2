class ApplicationController < ActionController::Base
  protect_from_forgery with: :exception
  protect_from_forgery with: :null_session
  protect_from_forgery prepend: true
  before_action :configure_permitted_parameters, if: :devise_controller?
  protected

  def configure_permitted_parameters
    added_attrs = [:email, :password, :password_confirmation,
    :remember_me, :name, :phone, :gender, :birthday, :address, :role_id]
    devise_parameter_sanitizer.permit :sign_in, keys: added_attrs
    devise_parameter_sanitizer.permit :sign_up, keys: added_attrs
    devise_parameter_sanitizer.permit :account_update, keys: added_attrs
  end

  def after_sign_in_path_for(resource)
    if resource.role_id == 1
      home_path 
    else
      reset_session
      '/422'
    end
  end

    # sign_in_url = new_user_session_url
    # if request.referer == sign_in_url
    #   super
    # else
    #   stored_location_for(resource) || request.referer || unauthenticated_path
    # end
end
