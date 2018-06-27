class CreateProducts < ActiveRecord::Migration[5.1]
  def change
    create_table :products do |t|
      t.string :product_key
      t.string :name
      t.integer :quantity
      t.string :manufacturer
      t.date :manu_date
      t.date :expired_date
      t.string :description
      t.float :price
      t.string :status, default: 'active'
      t.integer :updater_id

      t.belongs_to :category
      t.belongs_to :account
      t.timestamps
    end
  end
end
