class Api::V1::ApplicationController < ActionController::API
  include ActionController::HttpAuthentication::Basic::ControllerMethods
  include ActionController::ImplicitRender
  before_action :authenticate
  protect_from_forgery with: :null_session
  # respond_to :json
  protected
 
  def authenticate
    authenticate_or_request_with_http_basic do |username, password|
      username == "foo" && password == "bar"
    end
  end
 
  
  private
  def current_account
    account_email = request.query_parameters[:email].presence
    user = account_email && Account.find_by_email(account_email)
    
    if user && Devise.secure_compare(user.authentication_token, request.query_parameters[:user_token])
        user = Account.find_by_email(account_email)
      return user
    else
    render :json => '{"success" : "false"}'
    end
    
    # @current_account ||= Account.find_by authentication_token: request.headers["Authorization"] #token thông qua header
  end

  def authenticate_account_from_token
    account_email = params[:email].presence
    user       = account_email && Account.find_by_email(account_email)
 
    # Notice how we use Devise.secure_compare to compare the token
    # in the database with the token given in the params, mitigating
    # timing attacks.
    if user && Devise.secure_compare(user.authentication_token, params[:user_token])
        user = Account.find_by_email(account_email)
      return true
    else
    render :json => '{"success" : "false"}'
    end
  end
end

    # render json: {message: "You are not authenticated"},
    #   status: 401 if current_account.nil?
    # end

  # def ensure_params_exist
  #  return unless params[:account].blank?
  #   render json: {message: "Missing params"}, status: 422
  # end

  # def load_account_authentication
  #   @account = Account.find_by_email account_params[:email]
  #   return login_invalid unless @account
  # end

  # def login_invalid
  #   render json:
  #     {message: "Invalid login"}, status: 200
  # end