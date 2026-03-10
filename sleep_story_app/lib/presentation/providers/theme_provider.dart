import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

/// 主题模式 Provider
class ThemeProvider extends ChangeNotifier {
  final FlutterSecureStorage _storage = const FlutterSecureStorage();
  static const String _themeKey = 'theme_mode';

  ThemeMode _themeMode = ThemeMode.system;

  ThemeMode get themeMode => _themeMode;

  bool get isDarkMode => _themeMode == ThemeMode.dark;

  /// 初始化主题
  Future<void> initTheme() async {
    try {
      final themeStr = await _storage.read(key: _themeKey);
      if (themeStr != null) {
        _themeMode = ThemeMode.values.firstWhere(
          (mode) => mode.name == themeStr,
          orElse: () => ThemeMode.system,
        );
      }
    } catch (e) {
      _themeMode = ThemeMode.system;
    }
    notifyListeners();
  }

  /// 切换主题模式
  Future<void> setThemeMode(ThemeMode mode) async {
    _themeMode = mode;
    await _storage.write(key: _themeKey, value: mode.name);
    notifyListeners();
  }

  /// 切换暗黑模式
  Future<void> toggleDarkMode() async {
    final newMode = _themeMode == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
    await setThemeMode(newMode);
  }
}
