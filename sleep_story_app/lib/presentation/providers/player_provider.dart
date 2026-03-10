import 'package:flutter/foundation.dart';
import 'package:flutter_tts/flutter_tts.dart';

/// 播放状态
enum PlayerStatus {
  idle,
  loading,
  playing,
  paused,
  stopped,
  error,
}

/// 音频播放器 Provider
class PlayerProvider extends ChangeNotifier {
  final FlutterTts _tts = FlutterTts();

  PlayerStatus _status = PlayerStatus.idle;
  String? _currentStoryId;
  String? _currentText;
  double _progress = 0.0;
  double _duration = 0.0;
  double _position = 0.0;
  String? _errorMessage;

  // TTS 配置
  double _speed = 1.0;
  double _pitch = 1.0;
  String _language = 'zh-CN';

  PlayerStatus get status => _status;
  String? get currentStoryId => _currentStoryId;
  String? get currentText => _currentText;
  double get progress => _progress;
  double get duration => _duration;
  double get position => _position;
  String? get errorMessage => _errorMessage;
  double get speed => _speed;
  double get pitch => _pitch;
  String get language => _language;

  PlayerProvider() {
    _initTts();
  }

  Future<void> _initTts() async {
    // 配置 TTS
    await _tts.setLanguage(_language);
    await _tts.setSpeechRate(_speed);
    await _tts.setPitch(_pitch);

    // 监听进度
    _tts.setProgressHandler((text, start, end, word) {
      _position = start.toDouble();
      _duration = end.toDouble();
      if (_duration > 0) {
        _progress = _position / _duration;
      }
      notifyListeners();
    });

    // 监听完成
    _tts.setCompletionHandler(() {
      _status = PlayerStatus.stopped;
      _progress = 1.0;
      notifyListeners();
    });

    // 监听错误
    _tts.setErrorHandler((message) {
      _status = PlayerStatus.error;
      _errorMessage = message;
      notifyListeners();
    });
  }

  /// 播放文本（TTS）
  Future<void> playText(String storyId, String text) async {
    _status = PlayerStatus.loading;
    _currentStoryId = storyId;
    _currentText = text;
    _errorMessage = null;
    notifyListeners();

    try {
      await _tts.speak(text);
      _status = PlayerStatus.playing;
    } catch (e) {
      _status = PlayerStatus.error;
      _errorMessage = e.toString();
    }
    notifyListeners();
  }

  /// 暂停
  Future<void> pause() async {
    if (_status == PlayerStatus.playing) {
      await _tts.pause();
      _status = PlayerStatus.paused;
      notifyListeners();
    }
  }

  /// 继续播放
  Future<void> resume() async {
    if (_status == PlayerStatus.paused && _currentText != null) {
      await _tts.speak(_currentText!);
      _status = PlayerStatus.playing;
      notifyListeners();
    }
  }

  /// 停止
  Future<void> stop() async {
    await _tts.stop();
    _status = PlayerStatus.stopped;
    _progress = 0.0;
    _position = 0.0;
    notifyListeners();
  }

  /// 设置语速
  Future<void> setSpeed(double speed) async {
    _speed = speed;
    await _tts.setSpeechRate(speed);
    notifyListeners();
  }

  /// 设置音调
  Future<void> setPitch(double pitch) async {
    _pitch = pitch;
    await _tts.setPitch(pitch);
    notifyListeners();
  }

  /// 设置语言
  Future<void> setLanguage(String language) async {
    _language = language;
    await _tts.setLanguage(language);
    notifyListeners();
  }

  /// 清理资源
  @override
  void dispose() {
    _tts.stop();
    super.dispose();
  }
}
