class AccountsController < ApplicationController
  before_action :authenticate_account!, only: [:edit, :update, :change_password]

  def edit_change_password
  end
  
  def change_password
    @account = Account.find_by_id(current_account.id)
    old_password = params[:old_password]
    new_password = params[:new_password]
    confirm_password = params[:confirm_password]
    respond_to do |format|
      if BCrypt::Password.new(@account.encrypted_password) == old_password
        if new_password == confirm_password
          if @account.update_attributes(password: new_password)
            format.html {redirect_to root_path, notice: "Password updated successfully! Please login again"}
          else 
            format.html { redirect_to change_password_path(current_account), notice: 'Password is not match' }
            format.json { render json: { checkPassword: false } }
          end
        end
      else
        format.html { redirect_to change_password_path(current_account), notice: 'Current Password is invaild' }
        format.json { render json: { checkPassword: false } }
      end
    end
  end
  
  def index
    @accounts = Account.all.page(params[:page]).per(5)
    @roles = Role.all
  end

  def show
    @account = Account.find_by_id(params[:id])
  end
  
  def edit
    @user = Account.find_by_id(current_account.id)
  end

  def new
    @account = Account.new
  end
  
  def create
    random_password = Devise.friendly_token.first(8)
    # puts 'password random'
    # puts random_password
    @account = Account.new({
      email: params[:account][:email],
      password: random_password,
      name: params[:account][:name],
      role_id: 1
    })
    # puts @account.to_json
    respond_to do |format|
      if @account.save!
        @account.skip_confirmation!

        # sign_in current_account
        format.html { redirect_to accounts_path, notice: 'New Admin was successfully created.' }
        format.json { render :index, status: :created, location: @account }
      else
        format.html { redirect_to new_account_path }
        format.json { render json: @account.errors, status: :unprocessable_entity }
      end
    end
  end
  
  def update
    @account = Account.find_by_id(params[:id])
    @user = Account.find_by_id(current_account.id)

    if @account.id != current_account.id
      respond_to do |format|
        if @account.update(account_params)
          format.html { redirect_to accounts_path, notice: 'Account was successfully updated.' }
          format.json { render :show, status: :ok, location: @account }
        else
          format.html { render :edit }
          format.json { render json: @account.errors, status: :unprocessable_entity }
        end
      end
    else
      respond_to do |format|
        if @user.update(account_params)
          format.html { redirect_to edit_account_path(@user), notice: 'Account was successfully updated.' }
          format.json { render :edit, status: :ok, location: @user }
        else
          format.html { render :edit }
          format.json { render json: @user.errors, status: :unprocessable_entity }
        end
      end
    end
  end

  def search_accounts_by_name
    if params[:name]
      @accounts = Account.where("name LIKE ?", "%#{params[:name]}%")

      result = Array.new
      @accounts.each do |t|
        result.push({id: t.id, name: t.name, email: t.email, status: t.status.capitalize,
          role: Role.find(t.role_id).name.upcase})
      end
      render json: result.to_json
    end
  end
  
  def search_accounts_by_role
    if params[:role_id]
      @accounts = Account.where(role_id: params[:role_id])
      
      result = Array.new
      @accounts.each do |t|
        result.push({id: t.id, name: t.name, email: t.email, status: t.status.capitalize,
          role: Role.find(t.role_id).name.upcase})
      end
      render json: result.to_json
    end
  end


  private
    def account_params
      params.require(:account).permit(:email, :password, :name, :gender, :address, :phone, :role_id, :status)
    end
end
