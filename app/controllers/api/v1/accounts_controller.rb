class Api::V1::AccountsController < ActionController::API
  
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
  

  def account_params
    params.require(:account).permit(:name, :email, :phone, :address, :gender)
  end
end