class CreateOrderProducts < ActiveRecord::Migration[5.1]
  def change
    create_table :order_products do |t|
      t.float :price
      t.integer :quantity
      t.string :reason
      t.string :status, default: 'pending'

      t.belongs_to :order
      t.belongs_to :product

      t.timestamps
    end
  end
end
