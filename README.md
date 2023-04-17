# BetterCRM

BetterCRM is my Java practice project. It helps me in checking task completion emailStatus.

## Ý tưởng

Vì có rất nhiều lớp học và hàng ngày phải kiểm tra tiến độ gửi mail nhận xét học sinh của từng lớp. BetterCRM giúp công việc đó trở nên nhanh chóng hơn.

## Các tính năng

BetterCRM có thể tự động hiển thị tiết học gần nhất cùng với trạng thái gửi nhận xét của mỗi lớp, tìm kiếm lớp học theo tên để xem thông tin chi tiết các tiết học của lớp đó.

## Cách sử dụng

Ứng dụng có 2 tab là tab Dashboard, tab Admin, thanh tìm kiếm và các trang lớp học

1. **Thanh tìm kiếm**

Người dùng có thể tìm kiếm lớp học bằng các nhập tên của lớp.

2. **Tab Dashboard**

![Tab Dashboard](https://github.com/monkeydminh49/BetterCRM/blob/master/doc/img/dashboard_bettercrm.png?raw=true)

- Ở tab này, các lớp học được hiển thị với tên lớp học, tiết học gần nhất, thời gian tiết học,
  và trạng thái gửi nhận xét lớp. Nếu trạng thái là đã gửi sẽ hiện YES màu xanh, chưa gửi nếu chưa quá hạn gửi sẽ hiện
  NO màu vàng, nếu đã quá hạn nộp sẽ hiện OVERDUE màu đỏ.
- Bên cạnh các lớp học là các phím chức năng: xem chi tiết lớp học, tải lại trạng thái tiết học và xóa lớp học.
- Người dùng có thể chọn khoảng thời gian hiển thị lớp học bằng cách chọn khoảng thời gian ở phần lịch.
- Danh sách lớp học có thể được xem theo chiều mới nhất và cũ nhất bằng cách chọn nút mũi tên 2 chiều ở cạnh phần lịch.

3. **Tab Admin**

![Tab Admin](https://github.com/monkeydminh49/BetterCRM/blob/master/doc/img/admin_bettercrm.png?raw=true)

- Người dùng có thể cập nhật danh sách lớp thông qua nút "Update class list" khi có lớp học mới được thêm vào hệ thống.
- Thời gian lần cuối cùng cập nhật danh sách lớp học sẽ được hiển thị ở phần "Last update".

4. **Trang lớp học**

![Trang lớp học](https://github.com/monkeydminh49/BetterCRM/blob/master/doc/img/class_bettercrm.png?raw=true)

- Ở trang lớp học, người dùng có thể xem thông tin các tiết học của lớp học, TA và giáo viên từng tiết học của lớp.
- Người dùng có thể tải lại trạng thái lớp với nút "Reload" phía trên bên phải bảng các tiết học.
