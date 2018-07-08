
#################################################################################################################
insert into priviledges (name) values ('CREATE_USER');
insert into priviledges (name) values ('DELETE_USER');
insert into priviledges (name) values ('GET_ALL_USER');
insert into priviledges (name) values ('GET_USER');
insert into priviledges (name) values ('UPDATE_USER');

insert into roles (name) values ('ROLE_SUPER_ADMIN');
insert into roles (name) values ('ROLE_ADMIN');
insert into roles (name) values ('ROLE_USER');

##### Hash of password 123456 is $2a$10$FftR607XZoKaOaGIcgUdwe1P9EtaW0YxLcKqm7gm84BYVVFpfXiVq
insert into users(username,email,enabled,isAccountNonExpired,isAccountNonLocked,isCredentialsNonExpired,password) values('root','root@gmail.com',true,true,true,true,'$2a$10$FftR607XZoKaOaGIcgUdwe1P9EtaW0YxLcKqm7gm84BYVVFpfXiVq');
insert into users(username,email,enabled,isAccountNonExpired,isAccountNonLocked,isCredentialsNonExpired,password) values('yashpal','yashpal@gmail.com',true,true,true,true,'$2a$10$FftR607XZoKaOaGIcgUdwe1P9EtaW0YxLcKqm7gm84BYVVFpfXiVq');
insert into users(username,email,enabled,isAccountNonExpired,isAccountNonLocked,isCredentialsNonExpired,password) values('chahar','chahar@gmail.com',true,true,true,true,'$2a$10$FftR607XZoKaOaGIcgUdwe1P9EtaW0YxLcKqm7gm84BYVVFpfXiVq');
insert into users(username,email,enabled,isAccountNonExpired,isAccountNonLocked,isCredentialsNonExpired,password) values('pal','pal@gmail.com',true,true,true,true,'123456');

insert into role_priviledges(rolename,priviledgename) values ('ROLE_SUPER_ADMIN','CREATE_USER'),('ROLE_SUPER_ADMIN','DELETE_USER'),('ROLE_SUPER_ADMIN','GET_ALL_USER'),('ROLE_SUPER_ADMIN','GET_USER'),('ROLE_SUPER_ADMIN','UPDATE_USER');
insert into role_priviledges(rolename,priviledgename) values ('ROLE_ADMIN','GET_ALL_USER'),('ROLE_ADMIN','GET_USER'),('ROLE_ADMIN','UPDATE_USER');
insert into role_priviledges(rolename,priviledgename) values ('ROLE_USER','GET_USER');

insert into user_roles(username,rolename) values ('root','ROLE_SUPER_ADMIN'),('root','ROLE_ADMIN'),('root','ROLE_USER');
insert into user_roles(username,rolename) values ('yashpal','ROLE_ADMIN'),('yashpal','ROLE_USER');
insert into user_roles(username,rolename) values ('chahar','ROLE_USER');
insert into user_roles(username,rolename) values ('pal','ROLE_USER');

