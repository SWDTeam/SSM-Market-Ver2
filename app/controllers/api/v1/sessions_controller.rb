class Api::V1::SessionsController < Devise::SessionsController
  prepend_before_action :require_no_authentication, only: :create
  protect_from_forgery with: :null_session, only: [:create]
  skip_before_action :verify_authenticity_token, only: [:create]
  before_action :ensure_params_exist, only: :create
  
  respond_to :json
  #{"sesion": {"email" :}}
  def create
    user = Account.find_by_email(params[:session][:email])
    puts 'user'
    puts user.to_json
    unless user.nil?
      #&& user.role_id == 2
      if user.valid_password? params[:session][:password] 
        if user.role_id == 2
          render json: user.as_json.merge({email: user.email, name: user.name, success: true}).to_json, status: 200
          return
        end
      end
    end
    invalid_login_attempt
  end

  def destroy
    sign_out(account)
  end

  protected

  def ensure_params_exist
    # puts " aaaaaa " + params[:session][:email]
    return render json: {success: false, message: "missing user_login parameter"}, status: 422 if params[:session][:email].blank? || params[:session][:password].blank?
  end
  
  def invalid_login_attempt
    warden.custom_failure!
    render json: {success: false, message: "Error with your login or password"}, status: 401
  end
end
  