class CreateOrders < ActiveRecord::Migration[5.1]
  def change
    create_table :orders do |t|
      t.string :code
      t.datetime :payment_date
      t.string :address_ship
      t.float :total_price
      t.string :status
      t.integer :total_quantity

      t.belongs_to :account
      t.integer :cashier_id
      t.timestamps
    end
  end
end
