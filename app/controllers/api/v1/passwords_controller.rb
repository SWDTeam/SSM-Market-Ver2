class Api::V1::PasswordsController < ActionController::API
  prepend_before_action :require_no_authentication, only: :create
  # before_action :ensure_params_exist, only: :create
  
  def forgot
    if params[:email].nil?
      return render json: {error: 'Email not present'}
    end

    user = Account.find_by(email: params[:email].downcase)
    if user.present? && user.confirmed_at?
      user.generate_password_token!
      user.send_reset_password_instructions
      render json: {status: 'ok'}, status: :ok
    else
      render json: {error: ['Email address not found. Please check and try again.']}, status: :not_found
    end
  end

  def reset
    token = params[:token].to_s
    user = Account.find_by(reset_password_token: token)
    if user.present? && user.password_token_valid?
      if user.reset_password!(params[:password])
        render json: {status: 'ok'}, status: :ok
      else
        render json: {errors: user.errors.full_messages}, status: :unprocessable_entity
      end
    else
      render json: {errors: ['The email link seems to be invalid. Try requesting for a new one.']}, status: :not_found
    end
  end

end