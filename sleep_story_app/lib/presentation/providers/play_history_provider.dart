import 'package:flutter/foundation.dart';
import '../../data/models/play_history_model.dart';
import '../../data/repositories/play_history_repository_impl.dart';

/// 播放历史状态
enum PlayHistoryStatus {
  initial,
  loading,
  loaded,
  error,
}

/// 播放历史 Provider
class PlayHistoryProvider extends ChangeNotifier {
  final PlayHistoryRepositoryImpl _repository = PlayHistoryRepositoryImpl();

  PlayHistoryStatus _status = PlayHistoryStatus.initial;
  List<PlayHistoryModel> _history = [];
  String? _errorMessage;
  bool _hasMore = true;
  int _offset = 0;
  static const int _limit = 20;

  PlayHistoryStatus get status => _status;
  List<PlayHistoryModel> get history => _history;
  String? get errorMessage => _errorMessage;
  bool get hasMore => _hasMore;
  bool get isEmpty => _history.isEmpty && _status == PlayHistoryStatus.loaded;

  /// 加载播放历史
  Future<void> loadHistory({bool refresh = false}) async {
    if (_status == PlayHistoryStatus.loading) return;

    if (refresh) {
      _offset = 0;
      _history = [];
      _hasMore = true;
    }

    _status = PlayHistoryStatus.loading;
    _errorMessage = null;
    notifyListeners();

    try {
      final newHistory = await _repository.getPlayHistory(
        limit: _limit,
        offset: _offset,
      );

      if (newHistory.length < _limit) {
        _hasMore = false;
      }

      _history.addAll(newHistory);
      _offset += newHistory.length;
      _status = PlayHistoryStatus.loaded;
    } catch (e) {
      _errorMessage = e.toString();
      _status = PlayHistoryStatus.error;
    }

    notifyListeners();
  }

  /// 加载更多
  Future<void> loadMore() async {
    if (!_hasMore || _status == PlayHistoryStatus.loading) return;
    await loadHistory();
  }

  /// 刷新
  Future<void> refresh() async {
    await loadHistory(refresh: true);
  }

  /// 记录播放历史
  Future<void> recordHistory({
    required String storyId,
    required int durationListened,
  }) async {
    try {
      await _repository.recordPlayHistory(
        storyId: storyId,
        durationListened: durationListened,
      );
    } catch (e) {
      // 静默失败，不影响用户体验
      debugPrint('记录播放历史失败: $e');
    }
  }

  /// 删除单条历史
  Future<void> deleteHistory(String historyId) async {
    try {
      await _repository.deletePlayHistory(historyId);
      _history.removeWhere((item) => item.id == historyId);
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  /// 清空所有历史
  Future<void> clearHistory() async {
    try {
      await _repository.clearPlayHistory();
      _history = [];
      _offset = 0;
      _hasMore = true;
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }
}
