class Api::V1::RegistrationsController < Devise::RegistrationsController
  before_action :ensure_params_exist, only: [:create]

  respond_to :json
  # {"registration": {"email" : ""}}
  
  def create
    account = Account.new(account_params)
    puts "Params cua acccount " + account_params.to_s
    if account.save
      puts "THANH CONG ROI BE OI"      
      render json: account.as_json.merge({email: account.email, name: account.name, success: true}).to_json, status: 200
      return 
    end
    warden.custom_failure!
    puts "Vao warden nhe"
    render json: account.errors, status: 200
  end

  protected
  def ensure_params_exist
    if params[:registration][:email].blank? || params[:registration][:password].blank? || 
      params[:registration][:name].blank? || params[:registration][:role_id].blank? || 
      params[:registration][:gender].blank? || 
      params[:registration][:address].blank? || params[:registration][:phone].blank?
      render json: {success: false, message: "missing sign up parameter"}, status: 422 
    end
  end
  
  private
  def account_params
    params.require(:registration).permit(:email, :password, :name, :gender, :address, :phone, :role_id)
  end
end