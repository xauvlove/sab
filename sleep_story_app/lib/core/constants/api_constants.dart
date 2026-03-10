/// API 配置常量
class ApiConstants {
  ApiConstants._();

  // 基础 URL - 开发环境使用 localhost
  // Android 模拟器: 10.0.2.2 表示宿主机的 localhost
  // iOS 模拟器: localhost
  // 真机: 使用实际服务器 IP
  static const String baseUrl = 'http://10.0.2.2:8080/api';

  // Auth endpoints
  static const String sendSms = '/auth/sms/send';
  static const String smsLogin = '/auth/sms/login';
  static const String login = '/auth/login';
  static const String register = '/auth/register';
  static const String refreshToken = '/auth/refresh';
  static const String me = '/auth/me';
  static const String checkPhone = '/auth/check-phone';

  // Story endpoints
  static const String stories = '/stories';
  static const String storyDetail = '/stories/{id}';
  static const String homeStories = '/stories/home';
  static const String categories = '/categories';
  static const String search = '/stories/search';

  // Favorite endpoints
  static const String favorites = '/favorites';

  // User endpoints
  static const String userProfile = '/user/profile';
  static const String userStats = '/user/stats';
  static const String userPreferences = '/user/preferences';
  static const String playHistory = '/user/history';

  // Community endpoints
  static const String communityStories = '/community/stories';
  static const String communityFeed = '/community/feed';
  static const String communityDetail = '/community/stories/{id}';
  static const String communityLike = '/community/stories/{id}/like';
  static const String communityUserStories = '/community/stories/user/{userId}';
}

/// App 配置常量
class AppConstants {
  AppConstants._();

  static const String appName = '睡眠故事';
  static const String appVersion = '1.0.0';

  // Storage keys
  static const String tokenKey = 'auth_token';
  static const String userIdKey = 'user_id';
  static const String userInfoKey = 'user_info';
  static const String themeKey = 'theme_mode';
  static const String preferencesKey = 'user_preferences';

  // Default values
  static const int defaultPageSize = 20;
  static const int defaultLoginTimer = 60; // seconds
  static const int smsValidTime = 900; // 15 minutes
}
