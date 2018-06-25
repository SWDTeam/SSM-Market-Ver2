class CreateImages < ActiveRecord::Migration[5.1]
  def change
    create_table :images do |t|
      t.string :url
      t.string :url_out

      t.belongs_to :product
      t.belongs_to :category

      t.timestamps
    end
  end
end
