class AccountsController < ApplicationController
  before_action :authenticate_account!, only: [:edit, :update]

  def index
    @accounts = Account.all
  end

  def show
    
  end
  
  def edit
    
  end

  def new
    @account = Account.new
  end
  
  def create
    
  end
  
  def update
    # if @current_account.update(user_params)
    #   redirect_to edit_user_path(@current_user), flash: {notice: 'Profile updated sucessfully!'}
    # end
  end

  # def check_old_password
  #   @account = Account.find_by_id(current_account.id)
  #   old_password = params[:old_password]
  #   respond_to do |format|
  #     if BCrypt::Password.new(@account.encrypted_password) == old_password
  #       format.json { render json: {checkPassword: true } }
  #     else
  #       format.json { render json: { checkPassword: false } }
  #     end
  #   end
  # end
  def change_password
  
  end
  
  def edit_password
    @account = Account.find_by_id(current_account.id)
    if @account.update_attributes(password: params[:account][:new_password])
      redirect_to edit_account_path(current_account), flash: { notice: 'Change password sucessfully!' }
    else
      flash[:notice] = 'Change password failed'
    end
  end

  # check existed mail
  # def check_email
  #   email = params[:txt_email].downcase
  #   user = User.find_by_email(email)
  #   respond_to do |format|
  #   format.json { render json: { check: !user.nil? }}
  #   # true if email ready existed
  #   end
  # end

  private
    def account_params
      params.require(:account).permit(:name, :email, :phone, :address, :gender)
    end
end
