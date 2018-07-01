# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rails db:seed command (or created alongside the database with db:setup).
#
# Examples:
#
#   movies = Movie.create([{ name: 'Star Wars' }, { name: 'Lord of the Rings' }])
#   Character.create(name: 'Luke', movie: movies.first)

role_admin = Role.create!(name: "admin",description: "admin")
role_user = Role.create!(name: "user",description: "user")

random_password = Devise.friendly_token.first(8)
admin = Account.new({
    email: "minhtestcode@gmail.com", 
    password: random_password, 
    name: "Minh Bác Ái",
    role_id: role_admin.id
  })

admin.skip_confirmation!
admin.save!

admin_1 = Account.new({
    email: "tuankiet1797@gmail.com",
    password: random_password, 
    name: "Kiệt Đập Chai",
    role_id: role_admin.id
  })
admin_1.skip_confirmation!
admin_1.save!

user = Account.new(
  email: "minh.thu2831997@gmail.com",
  password: random_password,
  name: "Thư Thư",
  role_id: role_user.id
)

user.skip_confirmation!
user.save!
