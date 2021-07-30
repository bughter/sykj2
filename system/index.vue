<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="500px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="80px">
          <el-form-item label="酒店ID">
            <el-input v-model="form.hotelid" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="酒店名称">
            <el-input v-model="form.hotelname" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="联系人姓名">
            <el-input v-model="form.contactname" style="width: 370px;" />
          </el-form-item>
          <el-form-item label="联系人手机号">
            <el-input v-model="form.contactmobile" style="width: 370px;" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.status.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderno" label="酒店订单号" />
        <el-table-column prop="checkindate" label="入住日期" />
        <el-table-column prop="checkoutdate" label="离店日期" />
        <el-table-column prop="orderamount" label="订单总金额" />
        <el-table-column prop="orderstatus" label="订单状态（1=待付款，2=待确认，3=已确认，4=确认失败，5=已完成，6=取消中，7=取消失败，8=已取消）" />
        <el-table-column prop="paystatus" label="支付状态（0=待支付，1=已支付）" />
        <el-table-column prop="paytime" label="支付时间(支付成功才有值，格式：yyyy-MM-dd HH:mm:ss)" />
        <el-table-column prop="paymenttype" label="支付方式(0未支付 1虚拟钱包支付 2支付宝 3微信 暂时只支持虚拟钱包支付)" />
        <el-table-column prop="refundamount" label="订房失败或取消成功，退款金额" />
        <el-table-column prop="refundsuccesstime" label="订房失败或取消成功需退款，退款成功时间" />
        <el-table-column prop="hotelid" label="酒店ID" />
        <el-table-column prop="hotelname" label="酒店名称" />
        <el-table-column prop="contactname" label="联系人姓名" />
        <el-table-column prop="contactmobile" label="联系人手机号" />
        <el-table-column prop="createtime" label="下单时间" />
        <el-table-column v-if="checkPer(['admin','hotelOrder:edit','hotelOrder:del'])" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudHotelOrder from '@/api/hotelOrder'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'

const defaultForm = { id: null, orderno: null, numberofrooms: null, numberofnights: null, arrivetime: null, checkindate: null, checkoutdate: null, guestnames: null, remark: null, orderamount: null, orderstatus: null, paystatus: null, paytime: null, paymenttype: null, transactionno: null, refundamount: null, refundsuccesstime: null, refundtransactionno: null, refundtransactionmethods: null, hotelid: null, hotelname: null, hoteladdress: null, roomtypename: null, bedtypename: null, rateplanid: null, priceperday: null, breakfastdesc: null, cancelpolicytype: null, canceldesc: null, confirmationnumber: null, contactname: null, contactmobile: null, contactemail: null, paylimittime: null, createtime: null }
export default {
  name: 'HotelOrder',
  components: { pagination, crudOperation, rrOperation, udOperation },
  mixins: [presenter(), header(), form(defaultForm), crud()],
  cruds() {
    return CRUD({ title: '酒店订单', url: 'api/hotelOrder', idField: 'id', sort: 'id,desc', crudMethod: { ...crudHotelOrder }})
  },
  data() {
    return {
      permission: {
        add: ['admin', 'hotelOrder:add'],
        edit: ['admin', 'hotelOrder:edit'],
        del: ['admin', 'hotelOrder:del']
      },
      rules: {
      }    }
  },
  methods: {
    // 钩子：在获取表格数据之前执行，false 则代表不获取数据
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }
  }
}
</script>

<style scoped>

</style>
