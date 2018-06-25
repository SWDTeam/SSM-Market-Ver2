class Product < ApplicationRecord
  belongs_to :account

  has_many :images
  has_one :category
end
