class Category < ApplicationRecord
  belongs_to :account
  # belongs_to :product
  has_one :image
end
