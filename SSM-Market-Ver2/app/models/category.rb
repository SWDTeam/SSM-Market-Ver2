class Category < ApplicationRecord
  belongs_to :product
  has_one :image
end
