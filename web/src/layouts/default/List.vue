<template>
  <v-list
    expand
    nav
    v-bind="$attrs"
    v-on="$listeners"
  >
    <template v-for="(item, i) in items">
      <default-list-group
        v-if="item.items && validItems(item)"
        :key="`group-${i}`"
        :item="item"
      />

      <default-list-item
        v-if="!item.items && validItems(item)"
        :key="`item-${i}`"
        :item="item"
      />
    </template>
  </v-list>
</template>

<script>
  import { validItems } from '@/util/validpermiss'
  export default {
    name: 'DefaultList',

    components: {
      DefaultListGroup: () => import('./ListGroup'),
      DefaultListItem: () => import('./ListItem'),
    },

    props: {
      items: {
        type: Array,
        default: () => ([]),
      },
    },

    data () {
      return {
        validItems: validItems,
      }
    },
  }
</script>
