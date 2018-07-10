class Category < ApplicationRecord
  belongs_to :account
  # belongs_to :product
  has_many :images
  validates :name, uniqueness:  {message: 'Name had already!'}, on: [:create,:update]
end
