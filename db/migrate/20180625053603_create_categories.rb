class CreateCategories < ActiveRecord::Migration[5.1]
  def change
    create_table :categories do |t|
      t.string :name
      t.string :status, default: 'active'
      t.belongs_to :account
      t.timestamps
    end
  end
end
