class Api::V1::AccountsController < ActionController::API
  before_action :check_field_change_pass, only: [:change_password]

  def show
    user = Account.find_by_id(params[:id])
    unless user.nil?
      render json: user.as_json, status: 200 
      return      
    end
    render json: {errors: 'Not found'}, status: 404
  end

  def update
    user = Account.find_by_id(params[:id])
    return render json: {errors: 'Not found'}, status: 404 if user.nil?    
    unless user.nil?
      if user.update(account_params)
        render json: user.as_json, status: 200
        return
      else
        render json: user.errors, status: :unprocessable_entity 
      end
    end
  end
  
  def change_password
    account = Account.find_by_id(params[:account][:id])
    puts `change password:  #{account}`
    old_password = params[:account][:old_password]
    puts "old pass " + old_password
    if BCrypt::Password.new(account.encrypted_password) == old_password #check old pass match in db
      if account.update_attributes(password: params[:account][:new_password])
        render json: account.as_json.merge({success: true}).to_json, status: 200
        return
      end
      render json: account.errors, status: :unprocessable_entity 
    end
    render json: {message: "old password not match", success: false}
  end
  
  protected
  def check_field_change_pass
    if params[:account][:id].blank? || params[:account][:old_password].blank? || params[:account][:new_password].blank?    
      render json: {success: false, message: "missing change password parameter"}, status: 422       
      return
    end
  end

  private
  def account_params
    params.require(:account).permit(:name, :email, :phone, :address, :gender)
  end
end