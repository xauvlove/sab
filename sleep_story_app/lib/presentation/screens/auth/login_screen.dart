import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../core/theme/app_theme.dart';
import '../../providers/auth_provider.dart';

/// 登录页面
class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _phoneController = TextEditingController();
  final _codeController = TextEditingController();
  bool _showCodeInput = false;
  int _countdown = 0;

  @override
  void dispose() {
    _phoneController.dispose();
    _codeController.dispose();
    super.dispose();
  }

  /// 发送验证码
  Future<void> _sendCode() async {
    final phone = _phoneController.text.trim();
    if (phone.isEmpty || phone.length != 11) {
      _showError('请输入正确的手机号');
      return;
    }

    try {
      await context.read<AuthProvider>().sendSmsCode(phone);
      setState(() => _showCodeInput = true);
      _startCountdown();
    } catch (e) {
      _showError(e.toString());
    }
  }

  /// 验证码登录
  Future<void> _loginWithCode() async {
    final phone = _phoneController.text.trim();
    final code = _codeController.text.trim();

    if (phone.isEmpty || code.isEmpty) {
      _showError('请填写完整信息');
      return;
    }

    try {
      await context.read<AuthProvider>().smsLogin(phone, code);
    } catch (e) {
      _showError(e.toString());
    }
  }

  /// 倒计时
  void _startCountdown() {
    setState(() => _countdown = 60);
    Future.doWhile(() async {
      await Future.delayed(const Duration(seconds: 1));
      if (!mounted) return false;
      setState(() => _countdown--);
      return _countdown > 0;
    });
  }

  /// 显示错误
  void _showError(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: AppColors.error,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const SizedBox(height: 60),
              // Logo/图标
              Icon(
                Icons.nightlight_round,
                size: 80,
                color: AppColors.primary,
              ),
              const SizedBox(height: 24),
              // 标题
              Text(
                '睡眠故事',
                style: Theme.of(context).textTheme.headlineLarge,
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 8),
              Text(
                '每晚一个好梦',
                style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                      color: AppColors.textSecondary,
                    ),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 48),
              // 手机号输入
              TextField(
                controller: _phoneController,
                keyboardType: TextInputType.phone,
                maxLength: 11,
                decoration: const InputDecoration(
                  labelText: '手机号',
                  prefixIcon: Icon(Icons.phone_android),
                  counterText: '',
                ),
              ),
              const SizedBox(height: 16),
              // 验证码输入（显示后）
              if (_showCodeInput) ...[
                TextField(
                  controller: _codeController,
                  keyboardType: TextInputType.number,
                  maxLength: 6,
                  decoration: InputDecoration(
                    labelText: '验证码',
                    prefixIcon: const Icon(Icons.lock_outline),
                    counterText: '',
                    suffixIcon: TextButton(
                      onPressed: _countdown > 0 ? null : _sendCode,
                      child: Text(_countdown > 0 ? '${_countdown}s' : '重新发送'),
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                // 登录按钮
                Consumer<AuthProvider>(
                  builder: (context, auth, _) {
                    return ElevatedButton(
                      onPressed: auth.status == AuthStatus.loading
                          ? null
                          : _loginWithCode,
                      child: auth.status == AuthStatus.loading
                          ? const SizedBox(
                              height: 20,
                              width: 20,
                              child: CircularProgressIndicator(
                                strokeWidth: 2,
                                color: Colors.white,
                              ),
                            )
                          : const Text('登录'),
                    );
                  },
                ),
                const SizedBox(height: 16),
                // 返回按钮
                TextButton(
                  onPressed: () => setState(() => _showCodeInput = false),
                  child: const Text('返回'),
                ),
              ] else ...[
                // 发送验证码按钮
                ElevatedButton(
                  onPressed: _sendCode,
                  child: const Text('获取验证码'),
                ),
              ],
              const Spacer(),
              // 底部提示
              Text(
                '未注册的手机号将自动创建账号',
                style: Theme.of(context).textTheme.bodySmall,
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
