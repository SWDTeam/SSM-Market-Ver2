class Image < ApplicationRecord
  mount_uploader :url, ProductImageUploader  #upload img

  belongs_to :product, optional: true
  belongs_to :category, optional: true
end
