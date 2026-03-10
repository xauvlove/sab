import 'package:flutter/foundation.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'dart:convert';

/// 搜索历史 Provider（使用本地存储）
class SearchHistoryProvider extends ChangeNotifier {
  final FlutterSecureStorage _storage = const FlutterSecureStorage();
  static const String _storageKey = 'search_history';

  List<String> _history = [];
  static const int _maxHistoryCount = 20;

  List<String> get history => _history;
  bool get isEmpty => _history.isEmpty;

  /// 加载搜索历史
  Future<void> loadHistory() async {
    try {
      final data = await _storage.read(key: _storageKey);
      if (data != null && data.isNotEmpty) {
        final List<dynamic> decoded = jsonDecode(data);
        _history = decoded.cast<String>();
      }
    } catch (e) {
      debugPrint('加载搜索历史失败: $e');
      _history = [];
    }
    notifyListeners();
  }

  /// 添加搜索关键词
  Future<void> addSearch(String keyword) async {
    final trimmed = keyword.trim();
    if (trimmed.isEmpty) return;

    // 移除已存在的相同关键词
    _history.remove(trimmed);

    // 添加到开头
    _history.insert(0, trimmed);

    // 限制最大数量
    if (_history.length > _maxHistoryCount) {
      _history = _history.sublist(0, _maxHistoryCount);
    }

    await _saveHistory();
    notifyListeners();
  }

  /// 删除单个关键词
  Future<void> removeSearch(String keyword) async {
    _history.remove(keyword);
    await _saveHistory();
    notifyListeners();
  }

  /// 清空所有搜索历史
  Future<void> clearHistory() async {
    _history = [];
    await _storage.delete(key: _storageKey);
    notifyListeners();
  }

  Future<void> _saveHistory() async {
    try {
      await _storage.write(key: _storageKey, value: jsonEncode(_history));
    } catch (e) {
      debugPrint('保存搜索历史失败: $e');
    }
  }
}
