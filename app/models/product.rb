class Product < ApplicationRecord
  belongs_to :account
  has_many :images
  accepts_nested_attributes_for :images #upload img
  has_one :category

  validates :product_key, uniqueness:  {message: 'Product Key had already!'}, on: [:create,:update]
end
