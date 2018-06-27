class Api::V1::RegistrationsController < Devise::RegistrationsController
  before_action :ensure_params_exist

  respond_to :json
  #{"account": {"email" :}}
  
  def create
    account = Account.new(account_params)
    if account.save
      # render json: {message: "Registration has been completed", account: account}, status: 200
      render json: account.as_json.merge({email: account.email, name: account.name, success: true}).to_json, status: 200
      return
    else
      warden.custom_failure!
      render json: account.errors, status: 200
    end
  end


  protected

  def ensure_params_exist
    if params[:account][:email].blank? || params[:account][:password].blank? || 
      params[:account][:password_confirmation].blank? || params[:account][:name].blank? ||
      params[:account][:role_id].blank?
      render json: {success: false, message: "missing sign up parameter"}, status: 422 
    end
  end
  
  
  private
  def account_params
    params.require(:account).permit(:email, :password, :password_confirmation, :name, :role_id)
  end
end