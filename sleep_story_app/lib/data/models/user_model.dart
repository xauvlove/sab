import 'package:equatable/equatable.dart';

/// 用户实体
class User extends Equatable {
  final String id;
  final String phone;
  final String? email;
  final String? nickname;
  final String? avatarUrl;
  final String? status;
  final DateTime? lastLoginAt;
  final DateTime? createdAt;

  const User({
    required this.id,
    required this.phone,
    this.email,
    this.nickname,
    this.avatarUrl,
    this.status,
    this.lastLoginAt,
    this.createdAt,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'] ?? '',
      phone: json['phone'] ?? '',
      email: json['email'],
      nickname: json['nickname'],
      avatarUrl: json['avatarUrl'],
      status: json['status'],
      lastLoginAt: json['lastLoginAt'] != null
          ? DateTime.parse(json['lastLoginAt'])
          : null,
      createdAt: json['createdAt'] != null
          ? DateTime.parse(json['createdAt'])
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'phone': phone,
      'email': email,
      'nickname': nickname,
      'avatarUrl': avatarUrl,
      'status': status,
      'lastLoginAt': lastLoginAt?.toIso8601String(),
      'createdAt': createdAt?.toIso8601String(),
    };
  }

  User copyWith({
    String? id,
    String? phone,
    String? email,
    String? nickname,
    String? avatarUrl,
    String? status,
    DateTime? lastLoginAt,
    DateTime? createdAt,
  }) {
    return User(
      id: id ?? this.id,
      phone: phone ?? this.phone,
      email: email ?? this.email,
      nickname: nickname ?? this.nickname,
      avatarUrl: avatarUrl ?? this.avatarUrl,
      status: status ?? this.status,
      lastLoginAt: lastLoginAt ?? this.lastLoginAt,
      createdAt: createdAt ?? this.createdAt,
    );
  }

  @override
  List<Object?> get props => [
        id,
        phone,
        email,
        nickname,
        avatarUrl,
        status,
        lastLoginAt,
        createdAt,
      ];
}

/// 认证响应
class AuthResponse extends Equatable {
  final String token;
  final String? refreshToken;
  final User user;

  const AuthResponse({
    required this.token,
    this.refreshToken,
    required this.user,
  });

  factory AuthResponse.fromJson(Map<String, dynamic> json) {
    return AuthResponse(
      token: json['token'] ?? '',
      refreshToken: json['refreshToken'],
      user: User.fromJson(json['user'] ?? {}),
    );
  }

  @override
  List<Object?> get props => [token, refreshToken, user];
}
