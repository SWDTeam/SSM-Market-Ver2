class CreateRoles < ActiveRecord::Migration[5.1]
  def change
    create_table :roles do |t|
      t.string :name
      t.string :description
      t.string :status, default: 'active'

      t.timestamps
    end
  end
end
