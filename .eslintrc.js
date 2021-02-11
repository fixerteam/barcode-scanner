module.exports = {
  root: true,
  extends: ['@react-native-community', 'plugin:prettier/recommended', 'plugin:jest/recommended'],
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint'],
  rules: {
    semi: 0,
    'react/jsx-curly-brace-presence': [2, { props: 'never', children: 'ignore' }],
    'react-hooks/exhaustive-deps': 'warn',
    'eol-last': 1,
  },
  globals: {
    Intl: 'readonly',
  },
}
