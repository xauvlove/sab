package security

import (
	"golang.org/x/crypto/bcrypt"
)

// PasswordEncoder 密码加密器
type PasswordEncoder struct {
	cost int
}

// NewPasswordEncoder 创建密码加密器
func NewPasswordEncoder() *PasswordEncoder {
	return &PasswordEncoder{cost: bcrypt.DefaultCost}
}

// Encode 加密密码
func (e *PasswordEncoder) Encode(rawPassword string) (string, error) {
	hash, err := bcrypt.GenerateFromPassword([]byte(rawPassword), e.cost)
	if err != nil {
		return "", err
	}
	return string(hash), nil
}

// Matches 验证密码
func (e *PasswordEncoder) Matches(rawPassword, encodedPassword string) bool {
	err := bcrypt.CompareHashAndPassword([]byte(encodedPassword), []byte(rawPassword))
	return err == nil
}
