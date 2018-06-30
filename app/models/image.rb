class Image < ApplicationRecord
  mount_uploader :url, ProductImageUploader  #upload img
  mount_uploader :url, CategoryImageUploader

  belongs_to :product, optional: true
  belongs_to :category, optional: true
end
