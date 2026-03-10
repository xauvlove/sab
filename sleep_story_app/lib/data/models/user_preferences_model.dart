import 'package:equatable/equatable.dart';

/// 用户偏好设置
class UserPreferences extends Equatable {
  final bool darkMode;
  final int autoCloseTimer; // 分钟
  final int volumeLevel;
  final double defaultPlaySpeed;
  final bool enableNotifications;

  const UserPreferences({
    this.darkMode = false,
    this.autoCloseTimer = 30,
    this.volumeLevel = 50,
    this.defaultPlaySpeed = 1.0,
    this.enableNotifications = true,
  });

  factory UserPreferences.fromJson(Map<String, dynamic> json) {
    return UserPreferences(
      darkMode: json['darkMode'] ?? false,
      autoCloseTimer: json['autoCloseTimer'] ?? 30,
      volumeLevel: json['volumeLevel'] ?? 50,
      defaultPlaySpeed: (json['defaultPlaySpeed'] ?? 1.0).toDouble(),
      enableNotifications: json['enableNotifications'] ?? true,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'darkMode': darkMode,
      'autoCloseTimer': autoCloseTimer,
      'volumeLevel': volumeLevel,
      'defaultPlaySpeed': defaultPlaySpeed,
      'enableNotifications': enableNotifications,
    };
  }

  UserPreferences copyWith({
    bool? darkMode,
    int? autoCloseTimer,
    int? volumeLevel,
    double? defaultPlaySpeed,
    bool? enableNotifications,
  }) {
    return UserPreferences(
      darkMode: darkMode ?? this.darkMode,
      autoCloseTimer: autoCloseTimer ?? this.autoCloseTimer,
      volumeLevel: volumeLevel ?? this.volumeLevel,
      defaultPlaySpeed: defaultPlaySpeed ?? this.defaultPlaySpeed,
      enableNotifications: enableNotifications ?? this.enableNotifications,
    );
  }

  @override
  List<Object?> get props => [
        darkMode,
        autoCloseTimer,
        volumeLevel,
        defaultPlaySpeed,
        enableNotifications,
      ];
}
