import { createRouter, createWebHistory } from 'vue-router'

const Rutas = [
  {
    path: '/acceso',
    name: 'acceso',
    component: () => import('@/views/AccesoView.vue'),
    meta: { EsPublica: true }
  },
  {
    path: '/recuperar-contrasena',
    name: 'recuperar-contrasena',
    component: () => import('@/views/RecuperarContrasenaView.vue'),
    meta: { EsPublica: true }
  },
  {
    path: '/restablecer-contrasena',
    name: 'restablecer-contrasena',
    component: () => import('@/views/RestablecerContrasenaView.vue'),
    meta: { EsPublica: true }
  },
  {
    path: '/',
    component: () => import('@/layouts/AppLayout.vue'),
    meta: { RequiereAutenticacion: true },
    children: [
      {
        path: '',
        redirect: '/inicio'
      },
      {
        path: 'inicio',
        name: 'inicio',
        component: () => import('@/views/InicioView.vue')
      },
      {
        path: 'roles',
        name: 'roles',
        component: () => import('@/views/roles/RolesView.vue')
      },
      {
        path: 'usuarios',
        name: 'usuarios',
        component: () => import('@/views/usuarios/UsuariosView.vue')
      },
      {
  path: 'clientes',
  name: 'clientes',
  component: () => import('@/views/clientes/ClientesView.vue')
},
{
  path: 'proveedores',
  name: 'proveedores',
  component: () => import('@/views/proveedores/ProveedoresView.vue')
}
    ]
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/acceso'
  }
]

const Enrutador = createRouter({
  history: createWebHistory(),
  routes: Rutas
})

Enrutador.beforeEach((DestinoRuta) => {
  const TokenGuardado = localStorage.getItem('tokenJwt')
  const EstaAutenticado = !!TokenGuardado

  if (DestinoRuta.meta.RequiereAutenticacion && !EstaAutenticado) {
    return { name: 'acceso' }
  }
  if (DestinoRuta.meta.EsPublica && EstaAutenticado) {
    return { name: 'inicio' }
  }
})

export default Enrutador